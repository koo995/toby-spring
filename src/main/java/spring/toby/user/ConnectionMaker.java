package spring.toby.user;

import java.sql.Connection;

public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, java.sql.SQLException;
}
