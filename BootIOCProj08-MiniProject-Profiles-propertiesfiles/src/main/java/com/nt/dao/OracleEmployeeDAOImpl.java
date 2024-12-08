package com.nt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.nt.model.Employee;


@Repository("oraEmpDAO")
@Profile({"uat","prod","default"})
public class OracleEmployeeDAOImpl implements IEmployeeDAO {
	private static final String GET_EMPS_BY_DESGS="SELECT EMPNO,ENAME,JOB,SAL,DEPTNO FROM EMP WHERE JOB IN(?,?,?)ORDER BY JOB";
    @Autowired
    private DataSource ds;
	
    public OracleEmployeeDAOImpl() {
		System.out.println("OracleEmployeeDAOImpl::0-param constructor");
	
	}
	@Override
	public List<Employee> showAllEmployeeByDesgs(String desg1, String desg2, String desg3) throws Exception {
		System.out.println("EmployeeDAOImpl.showAllEmployeesByDesgs() ds obj class name:"+ds.getClass());
        List<Employee> list=null;
        try(Connection con=ds.getConnection();
        	PreparedStatement ps = con.prepareStatement(GET_EMPS_BY_DESGS);
        		){
        	//set values to query params
        	ps.setString(1, desg1);ps.setString(2, desg2);ps.setString(3, desg3);
        	//execute the Query
        	try(ResultSet rs=ps.executeQuery()){
        		list= new ArrayList();
        		while(rs.next()) {
        		//copy each record of RS to Employee class obj
        		Employee emp = new Employee();
        		emp.setEmpno(rs.getInt(1));
        		emp.setEname(rs.getString(2));
        		emp.setJob(rs.getString(3));
        		emp.setSal(rs.getFloat(4));
        		emp.setDeptno(rs.getInt(5));
        		//add each Employee class obj to list collection
        		list.add(emp);
        		}//while
        	}//try
        	
        }//try
        catch(SQLException se) {
        	se.printStackTrace();
        	throw se;
        }
        
        catch(Exception e) {
        	e.printStackTrace();
        	throw e;
        }
		return list;
	}

}
