<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head></head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员注册</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<!-- 表单验证插件 -->
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />
<script type="text/javascript">

     //自定义校验规则
     $.validator.addMethod(
		
		//验证规则的名称
		"checkUsername",
		//验证的函数
		function(value,element,params){
	       //value :输入的内容
	       //element:被校验的元素对象
	       //params:规则对应的参数
	       //目的：对输入的username进行校验，Ajax
	       var flag = false;
	       $.ajax({
	    	   "async":false,//同步
	    	   "url":"${pageContext.request.contextPath}/checkUsername",
	    	   "data":{"username":value},
	    	   "dataType":"json",	    	   
	    	   "type":"POST",
	    	   "success":function(data){
	    		   flag = data.isExit;
	    	   }  
	       });
		//如果flag为true:表示用户名已存在，则显示验证，为flase表示用户不存在，不显示
		//返回false才会显示验证
		return !flag;
			
});

 $(function(){
	 $("#myform").validate({
		rules:{
			"username":{
				"required":true,
				"checkUsername":true 
			},
			"password":{
				"required":true,
				"rangelength":[6,11]
			},
			"repassword":{
				"required":true,
				"equalTo":"#password"
			},
			"email":{
				"required":true,
				"email":true
			},
			"sex":{
				"required":true
			}
		},
		messages:{
            "username":{
            	"required":"用户名不能为空",
            	"checkUsername":"用户已存在"
            },			
		    "password":{
		    	"required":"密码不能为空！",
		    	"rangelength":"密码长度为6-11位"
		    },
		    "repassword":{
		    	"required":"密码不能为空！",
		    	"equalTo":"密码不一致，请重新输入"
		    },
		    "email":{
		    	"required":"邮箱不能为空",
		    	"email":"邮箱格式不正确"
		    }
		}
	 });
 });
</script>

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

font {
	color: #3164af;
	font-size: 18px;
	font-weight: normal;
	padding: 0 10px;
}

.error{
   color:red;
}
</style>
</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>会员注册</font>USER REGISTER
				<form id="myform" class="form-horizontal" style="margin-top: 5px;" action="${pageContext.request.contextPath}/register" method="post">
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" name="username"
								placeholder="请输入用户名">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="password"
								placeholder="请输入密码" name="password">
						</div>
					</div>
					<div class="form-group">
						<label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="repassword"
								placeholder="请输入确认密码" name="repassword">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
						<div class="col-sm-6">
							<input type="email" class="form-control" id="inputEmail3"
								placeholder="Email"  name="email">
						</div>
					</div>
					<div class="form-group">
						<label for="usercaption" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="usercaption"
								placeholder="请输入姓名" name="name">
						</div>
					</div>
					<div class="form-group opt">
						<label for="inlineRadio1" class="col-sm-2 control-label">性别</label>
						<div class="col-sm-6">
							<label class="radio-inline"> <input type="radio"
								 id="inlineRadio1" value="male" name="sex">
								男
							</label> <label class="radio-inline"> <input type="radio"
								 id="inlineRadio2" value="female" name="sex">
								女
							</label>
							<label for="sex" class="error" style="display:none">您没有第三种性别可选</label>
						</div>
					</div>
					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" class="form-control" name="birthday">
						</div>
					</div>

					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">验证码</label>
						<div class="col-sm-3">
							<input type="text" class="form-control" name="checkcode">

						</div>
						<div class="col-sm-2">
							<img src="./image/captcha.jhtml" />
						</div>

					</div>

					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" width="100" value="注册" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>




