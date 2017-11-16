<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/bootstrap-3.3.5-dist/css/bootstrap.min.css">
	<script src="${pageContext.request.contextPath }/js/jquery-3.1.0.min.js"></script>
	<script src="${pageContext.request.contextPath }/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script>
    $(document).ready(function(){
   		 $.post("${pageContext.request.contextPath }/CommonServlet",
   		 {
   			 method:"load",
   		 },
   		 function(data){
			 var dataobj=eval("("+data+")");
   			 $("#cars").html("");//清空cars内容
   			for(var i=0;i<dataobj.length;i++){
   				$("#cars").append("<option value="+dataobj[i].common+">");
   			}
   		 });
   		 
   		$.post("${pageContext.request.contextPath }/WeatherServlet",
   	   		 {
   	   			cityId:"101280601",
   	   		 },
   	   		 function(data){
   	   		 var dataobj=eval("("+data+")");
   	   		 for(var i=0;i<dataobj.length;i++){
   	   			$("#weatherlist").append("<h>"+dataobj[i].city+"天气&nbsp;&nbsp;&nbsp"+dataobj[i].date_y+"&nbsp;&nbsp;&nbsp"+dataobj[i].week+"&nbsp;&nbsp;&nbsp"+dataobj[i].weather+"&nbsp;&nbsp;&nbsp"+dataobj[i].temp1+"&nbsp;&nbsp;&nbsp"+dataobj[i].temp2+"</h>");
   	   		 }
   	      	  });
    	
    	$("#add").click(function(){
    		 $.post("${pageContext.request.contextPath}/CommonServlet",
    		 {
    			 method:"save",
    			 common:$("#addcommon").val(),
    		 },
    		 function(data){
    			 var dataobj=eval("("+data+")");
       			 $("#cars").html("");//清空cars内容
       			for(var i=0;i<dataobj.length;i++){
       				$("#cars").append("<option value="+dataobj[i].common+">");
       			}
       		 });
    	});
    	$("#delete").click(function(){
   		 $.post("${pageContext.request.contextPath}/CommonServlet",
   		 {
   			 method:"delete",
   			 deletecommon:$("#deletecommon").val(),
   		 },
   		 function(data){
   			 var dataobj=eval("("+data+")");
      			 $("#cars").html("");//清空cars内容
      			for(var i=0;i<dataobj.length;i++){
      				$("#cars").append("<option value="+dataobj[i].common+">");
      			}
      		 });
   	});
    });
</script>
  </head> 
  <body>
    <div class="container">
    <div class="row">
    <div class="col-lg-12" style="height:100px; background:#CF9">
     <div class="row">
       <div class="col-lg-8" id="weatherlist" style="padding-left:40px; margin-top:20px;">
       </div> 
     </div>
    </div>
    <div class="col-lg-12">
      <div style="float:right;">
       <form action="${pageContext.request.contextPath }/MongodbServlet" method="post">
         <input type="hidden" name="method" value="deletedb"/>
         <button class="btn btn-default" style="background:#396;">清空</button>
       </form>
      </div>
       <a style="float:right;  width:80px; height:32px; line-height:32px; margin-right:20px; color:#000; border-radius:4px; background:#396; text-decoration:none" href="http://mtest.kingdee.com/bjapp/mbos_store_20160815010001.tar.gz">下载最新包</a>
     </div>
    <!-- 输入搜索类型 -->
    <div class="col-lg-12" style="border-bottom:1px solid #F30; margin-top:20px;">
    <h style=" font-size:20px;">搜索类型</h>
    </div>
    <div class="col-lg-12" style="margin-top:20px;">
      <form class="form-inline" role="form" action="${pageContext.request.contextPath }/SearchServlet" method="post">
      <!-- 选择文件类型 -->
        <div class="form-group col-lg-12" style="border:1px solid #CCCCCC; padding:8px;">
          <label style="width:75px; text-align:center;">文件类型：</label>
          <div class="checkbox">
            <label class="checkbox-inline">
              <input type="checkbox" id="type" name="type" value=".js">
              js </label>
            <label class="checkbox-inline">
              <input type="checkbox" id="type" name="type" value=".css">
              css </label>
            <label class="checkbox-inline">
              <input type="checkbox" id="type" name="type" value=".server">
              server </label>
            <label class="checkbox-inline">
              <input type="checkbox" id="type" name="type" value="ui">
              page </label>
          </div>
        </div>
        <!-- 输入搜索关键字 -->
        <div class="form-group col-lg-12" style="border:1px solid #CCCCCC; margin-top:10px; padding:8px;">
          <label style="width:75px; text-align:center;">关键字：</label>
          <input type="text" id="keyword" name="keyword" placeholder="请输入关键字" />
        </div>
        <!-- 输入常见的类型 -->
        <div class="form-group col-lg-12" style=" border:1px solid #CCCCCC; margin-top:10px; padding:8px;">
          <div class="form-group">
            <label for="name" style="width:75px; text-align:center;">常用的:</label>
            <input list="cars" style="width:140px;" id="common" name="common"/>
              <datalist id="cars"> 
              </datalist>
              
            <input list="cars" style="width:140px;" id="deletecommon" name="deletecommon"/>
              <datalist id="cars">
              </datalist>
            <button id="delete" type="button" class="btn btn-default" style="background:#396; height:30px;">删除</button>
            
            <input type="text" id="addcommon" name="addcommon" style="width:100px;"/>
            <button id="add" type="button" class="btn btn-default" style="background:#396; height:30px;">添加</button>
          </div>
        </div>
        <div class="col-lg-12">
            <div style="text-align:right; margin-top:10px;"><button style="background:#396;" class="btn btn-default" type="submit">搜索</button></div>
         </div>
      </form>
    </div>
   
    <!-- 显示搜索 结果 -->
   <div class="col-lg-12">
   <h style=" font-size:20px; float:left;">搜索结果</h>
   <ul id="myTab" class="nav nav-tabs" style="float:left; margin-left:20px;">
   <li class="active">
      <a href="#result1" data-toggle="tab">
                     结果1
      </a>
   </li>
   <li><a href="#result2" data-toggle="tab">结果2</a></li>
  </ul>
  <div id="myTabContent" class="tab-content">
   <div class="tab-pane fade in active" id="result1">
   <div class="col-lg-12" style="height:500px; overflow:auto; border:2px solid #F30">
   <table class="table  table-bordered">
      <thead>
        <tr>
          <th>企业号</th>
          <th>文件名</th>
          <th>行号</th>
          <th>内容</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="list" items="${resultList}" >
         <tr>
          <td>${list.companyId}</td>
          <td>${list.fileName}</td>
          <td>${list.lineNumber}</td>
          <td>${list.content }</td>
          </tr>
         </c:forEach>
      </tbody>
    </table>
   </div>
   </div>
   <div class="tab-pane fade" id="result2">
     <div class="col-lg-12" style="height:500px; overflow:auto; border:2px solid #F30">
        <table class="table  table-bordered">
      <thead>
        <tr>
          <th>企业号</th>
          <th>记录</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="maplist" items="${map}" >
         <tr>
          <td>${maplist.key}</td>
          <td>${maplist.value}</td>
          </tr>
         </c:forEach>
      </tbody>
    </table>
     </div>
   </div>
 </div>
 </div>

  <div class="col-lg-3 col-md-offset-10" style="margin-bottom:20px;">
      <!-- 将搜索数据存入mongodb数据库 -->
      <form action="${pageContext.request.contextPath }/MongodbServlet" method="post" style="float:left;">
        <input type="hidden" name="method" value="savedb"/>
        <button class="btn btn-default" style="background:#396;">保存</button>
      </form>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <!-- 将搜索结果导出到EXCEL -->
      <form action="${pageContext.request.contextPath }/OutputExcel " method="post" style="float:left; margin-left:20px;">
        <button class="btn btn-default" style="background:#396;">导出Excel</button>
      </form>
   </div>
   
  <div class="col-lg-12" style="height:120px; background:#CF9">
  </div>
 </div>
</div>
</body>
  
</html>
