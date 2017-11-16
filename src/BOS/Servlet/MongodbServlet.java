package BOS.Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BOS.Dao.SearchResultDao;
import BOS.Result.SearchResult;

public class MongodbServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method=request.getParameter("method");
		
		if("savedb".equals(method)){
			savedb(request,response);
		}else if("deletedb".equals(method)){
			delete(request,response);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	//删除mongodb中所有的数据记录
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		SearchResultDao MS = new SearchResultDao();
		MS.deleteDB();
		try {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//将搜索记录插入到mongodb
	private void savedb(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ArrayList<SearchResult> list = (ArrayList<SearchResult>) session
				.getAttribute("resultList");
		System.out.println("-------list的大小为" + list.size() + "------------");
		SearchResultDao MS = new SearchResultDao();
		MS.insertIntoMongodb(list);
		try {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
