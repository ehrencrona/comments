
var Waiter = function(callback) {
	this.callbacks = [callback];
}

Waiter.prototype.done = function(value) {
	this.value = value;
	
	this.callbacks.foreach(function(callback) {
		callback(value);
	});
	
	this.callbacks = [];
}

Waiter.prototype.wait = function(callback) {
	if (this.value != null) {
		callback(this.value);
	}
	else {
		this.callbacks.push(callback);
	}
}

var withTemplates = function(allTemplates) {
	// TODO The need to store partials here disappears with Handlebars
	var templates = {};
	var waiter = null;

	return function(callback) {
		if (waiter != null) {
			waiter.wait(callback);
		}
		else {
			waiter = new Waiter(callback);
			
			// TODO: mash all templates into a single file.
			var allTemplates = ["commentlist", "comment", "reply", "poster", "login", "like", "loggedin", "profile"];

			templates = {};
			var loaded = 0;
			
			allTemplates.foreach(function(templateName) {
				// TODO remove hard-coded context root "/"
				$.ajax({
			        url: "/ms/" + templateName + ".ms",
			        success: function(template) {
			        	console.log("Loaded " + templateName);

			        	templates[templateName] = Handlebars.compile(template);
			        	Handlebars.registerPartial(templateName, templates[templateName]);

			        	if (++loaded == allTemplates.length) {
			        		waiter.done(templates);
			        	}
			        },
			        dataType: "text"
			    });
			});
		}
	}
}();
