package main.interfaces;

import main.entities.Key;
import main.entities.Resource;
import main.entities.Token;
import main.entities.User;

import java.util.Date;
import java.util.List;

public interface Auth {
    List<Resource> getListRes();
    Key getAuthentication(String userID,Date deadline,int lvl);
    Token getAuthorization(Key key, String resourceName);          //client hasn't Resource entity. It will give the name of res and server will check manually
    long checkToken(Token token);
    void clearAuth(int i);
    void addRes(Resource res);
    void deleteRes(Resource res);
}
