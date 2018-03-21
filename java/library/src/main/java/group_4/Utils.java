package group_4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import group_4.adapters.*;
import group_4.entities.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static com.google.gson.stream.JsonToken.END_DOCUMENT;

public class Utils {

    /* get gsonBuilder */
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Elem.class, new ElemAdapter());
        gsonBuilder.registerTypeAdapter(Request.class, new RequestAdapter());
        gsonBuilder.registerTypeAdapter(ErrorObj.class, new ErrorAdapter());
        gsonBuilder.registerTypeAdapter(Response.class, new ResponseAdapter());
        gsonBuilder.registerTypeAdapter(Notification.class, new NotificationAdapter());
        return gsonBuilder.create();
    }

    //Invoke this method to catch ParseErrors
    public static boolean isValidJSON(final String json) {
        return isValidJSON(new StringReader(json));
    }

    private static boolean isValidJSON(final Reader reader)  {
        return isValidJSON(new JsonReader(reader));
    }

    private static boolean isValidJSON(final JsonReader jsonReader) {
        try {
            JsonToken token;
            while ( (token = jsonReader.peek()) != END_DOCUMENT && token != null ) {
                switch ( token ) {
                    case BEGIN_ARRAY:
                        jsonReader.beginArray();
                        break;
                    case END_ARRAY:
                        jsonReader.endArray();
                        break;
                    case BEGIN_OBJECT:
                        jsonReader.beginObject();
                        break;
                    case END_OBJECT:
                        jsonReader.endObject();
                        break;
                    case NAME:
                        jsonReader.nextName();
                        break;
                    case STRING:
                    case NUMBER:
                    case BOOLEAN:
                    case NULL:
                        jsonReader.skipValue();
                        break;
                    case END_DOCUMENT:
                        return isValidJSON(jsonReader);
                    default:
                        throw new AssertionError(token);
                }
            }
            return true;
        } catch ( final MalformedJsonException ignored ) {
            return false;
        } catch (IOException e) { //We weren't able to parse this. We assume this isn't valid json.
            return false;
        }
    }

}
