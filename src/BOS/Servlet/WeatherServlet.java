package BOS.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import BOS.Dao.Weather;

public class WeatherServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cityId = request.getParameter("cityId");
		Weather weather = new Weather(cityId);
		Map<String, Object>weatherList = weather.getWeatherList();
		JSONArray array=JSONArray.fromObject(weatherList);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(array.toString());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
