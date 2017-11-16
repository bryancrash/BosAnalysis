package BOS.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import BOS.Dao.CommonDao;

public class CommonServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("save".equals(method)) {
			save(request, response);
		} else if ("load".equals(method)) {
			load(request, response);
		}else if("delete".equals(method)){
			delete(request,response);
		}

	}

	private void load(HttpServletRequest request, HttpServletResponse response) {
		CommonDao commondao = new CommonDao();
		JSONArray json = commondao.find();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		String addcommon = request.getParameter("common");
		CommonDao commondao = new CommonDao();
		
		commondao.insertCommon(addcommon);
		JSONArray json = commondao.find();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		String deletecommon=request.getParameter("deletecommon");
		CommonDao commondao=new CommonDao();
		commondao.deleteCommon(deletecommon);
		JSONArray json = commondao.find();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
