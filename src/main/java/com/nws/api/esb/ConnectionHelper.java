package com.nws.api.esb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.nws.api.utils.DBUtils;


@Component
public class ConnectionHelper {
	
	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(DBUtils.properties.get(IConstants.ORACLE_CLASSNAME));
			con = DriverManager.getConnection(DBUtils.properties.get(IConstants.ORACLE_DBURL),
					DBUtils.properties.get(IConstants.ORACLE_DBUSERNAME),
					DBUtils.properties.get(IConstants.ORACLE_DBPASSWORD));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void releaseConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
