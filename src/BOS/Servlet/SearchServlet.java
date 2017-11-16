package BOS.Servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BOS.Dao.SearchFileByKeyword;
import BOS.Dao.SearchFileByPattern;
import BOS.Dao.SearchResultDao;
import BOS.Result.SearchResult;

public class SearchServlet extends HttpServlet {

	@SuppressWarnings("deprecation")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 接受参数
		File file = new File("E:/mbos/store");
		System.out.println("开始处理数据");
		String type = request.getParameter("type");
		String keyword = request.getParameter("keyword");
		String common = request.getParameter("common");
		HttpSession session = request.getSession();
		List<SearchResult> list = null;

		// 判断传过来的数据类型，根据类型选择查找方式
		if ((keyword != null || keyword.length() != 0)
				&& (common == null || common.length() == 0)) {
			SearchResultDao ms = new SearchResultDao();

			// 从mongodb查找一条记录，判断是否存在相关记录，如果存在则从数据库提取记录，如果不存在则进行插入操作
			boolean bol = ms.findOneResult(type, keyword);
			if (bol) {
				list = ms.findList(type, keyword);
			} else {
				SearchFileByKeyword SF = new SearchFileByKeyword();
				try {
					list = SF.SearchKeyWord(file, type, keyword);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if ((keyword == null || keyword.length() == 0)
				&& (common != null || common.length() != 0)) {
			SearchResultDao ms = new SearchResultDao();
			boolean bol = ms.findOneResult(type, common);
			if (bol) {
				list = ms.findList(type, common);
			} else {

				SearchFileByPattern sfp = new SearchFileByPattern();
				try {
					list = sfp.SearchPattern(file, type, common);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if((keyword != null || keyword.length() != 0)
				&& (common != null || common.length() != 0)){
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		
		Map<String,Integer> map=new TreeMap<String,Integer>();
		for(int i=0;i<list.size();i++){
			String key=list.get(i).getCompanyId();
			if(!map.containsKey(key)){
				map.put(key,1);
			}else{
				map.put(key,map.get(key)+1);
			}
		}
		
		List<Map.Entry<String, Integer>> maplist=new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		//通过比较器来进行比较
		Collections.sort(maplist,new Comparator<Map.Entry<String,Integer>>(){

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
		
	
		session.setAttribute("resultList", list);
		session.setAttribute("map",maplist);
		System.out.println("list的大小" + list.size());
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
