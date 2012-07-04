var currentUser;

var showLoginForm = function() {
	withTemplates(function(templates) {
		output = templates.login();
	
		$("#login").html(output);
		$("#loginFormButton").click(function () {
	    	$.ajax({
	        	url: "/service/auth",
		        type: "POST",
		        data: "{\"password\":\"" + $("#loginFormPassword").val() + 
		        	"\", \"user\" : \"" + $("#loginFormUser").val() + "\"}",
		        dataType: "json",
		        success: function(json) {
		        	setCurrentUser(new Profile().fromJson(json));
// TODO show error message on login failure.			        	
		        }
		    });
		});
	});
}

var showLoggedIn = function() {
	withTemplates(function(templates) {
		var output = templates.loggedin(currentUser);
		$("#login").html(output);

		$("#logoutFormButton").click(function () {
	    	$.ajax({
	        	url: "/service/auth/logout",
		        type: "POST",
		        data: "{ }",
		        dataType: "json",
		        success: function(json) {
		        	setCurrentUser(new Profile().fromJson(json));
		        }
		    });
		});
	});
}

var setCurrentUser = function(newCurrentUser) {
	var fireEvent = currentUser != null;
	
	if (currentUser && currentUser.id === newCurrentUser.id) {
		return;
	}
	
	currentUser = newCurrentUser;
	
	if (!currentUser.anonymous()) {
		showLoggedIn();
	}
	else {
		showLoginForm();
	}
	
	if (fireEvent) {
		fireUserChange();
	}
};

(function (exports) {
	var userChangeListeners = [];

	exports.onUserChange = function(listener) {
		userChangeListeners.push(listener);
	};
	
	exports.fireUserChange = function() {
		userChangeListeners.foreach(function(listener) { listener.call(); });
	};
})(this);

