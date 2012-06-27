var loginInfo;

var showLoginForm = function() {
	withTemplates(function(templates) {
		output = Mustache.render(templates.login, {}, templates);
	
		$("#login").html(output);
		$("#loginFormButton").click(function () {
	    	$.ajax({
	        	url: "/service/auth",
		        type: "POST",
		        data: "{\"password\":\"" + $("#loginFormPassword").val() + 
		        	"\", \"user\" : \"" + $("#loginFormUser").val() + "\"}",
		        dataType: "json",
		        success: function(json) {
		        	loginInfo = json;

// show error message on login failure.			        	
		        	
					showLoggedIn();
		        }
		    });
		});
	});
}

var showLoggedIn = function() {
	withTemplates(function(templates) {
		var output = Mustache.render(templates.loggedin, loginInfo, templates);
		$("#login").html(output);

		$("#logoutFormButton").click(function () {
	    	$.ajax({
	        	url: "/service/auth/logout",
		        type: "POST",
		        data: "{ }",
		        dataType: "json",
		        success: function(json) {
		        	loginInfo = json;

		        	console.log(dump(json));
					showLoginForm();
		        }
		    });
		});
	});
}

$(document).ready(function() {
    $.ajax({
        url: "/service/auth",
        dataType: "json",
        success: function(json) {
        	loginInfo = json;
        	
    		if (json.loggedIn) {
    			showLoggedIn();
        	}
        	else {
        		showLoginForm();
        	}
        }
    });

});
