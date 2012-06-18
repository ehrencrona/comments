Array.prototype.foreach = function( callback ) {
    for(var k = 0; k < this.length; k++ ) {
        callback( this[ k ] );
    }
}

Array.prototype.prepend = function( item ) {
	var result = [];
	
	result.push(item);

	this.foreach(function(item) {
		result.push(item);
	});
	
	return result;
}

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

var withTemplates = function() {
	var templates = {};
	var waiter = null;

	return function(callback) {
		if (waiter != null) {
			waiter.wait(callback);
		}
		else {
			waiter = new Waiter(callback);
			
			var allTemplates = ["commentlist", "comment", "reply"];

			templates = {};
			var loaded = 0;
			
			allTemplates.foreach(function(templateName) {
				$.ajax({
			        url: templateName + ".ms",
			        success: function(template) {
			        	console.log("Loaded " + templateName);

			        	templates[templateName] = template;
			        		        	
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

var commentList = new CommentList(commentList);

var withComments = function() {
	var loaded = {};
	
	return function(url, callback) {
		if (loaded[url]) {
			callback(commentList);
		}
		else {
		    $.ajax({
		        url: url,
		        dataType: "json",
		        success: function(json) {
		        	console.log("Loaded " + url);
		        	loaded[url] = true;
		        	
					commentList.fromJson(json);
	
					callback(commentList);
		        }
		    });
		}
	};
}();

var initializeCommentList = function(commentList) {	
	withTemplates(function(templates) {
		renderComments(templates); 
	});

	return commentList;
};

var getCommentForm = function() {
	var cache = null;
	
	return function() {
		if (cache == null) {
			cache = $("#replyForm"); 
		}
		
		return cache;
	}
}();

CommentList.prototype.element = function() {
	return $("#comments");
}

Posting.prototype.element = function() {
	return $("#" + this.id);
};

var ignoreMe = function() {
	var hasForm = null;
	
	Posting.prototype.hasOpenReplyForm = function() {
		return this === hasForm;
	};
		
	Posting.prototype.closeReplyForm = function() {
		$(".postButton", this.element()).html("Reply");
		
		getCommentForm().hide();
		
		hasForm = null;
	};

	Posting.prototype.openReplyForm = function() {
		if (hasForm != null) {
			hasForm.closeReplyForm();
		}
		
		$(".postButton", this.element()).html("Close");
	    $("#replyFormReplyTo").val(this.id);
	
	    getCommentForm().show();
	    getCommentForm().appendTo(this.element());
	    
	    hasForm = this;
	};

}();

Posting.prototype.next = function() {
	var nextElement = this.element().next();
	
	if (nextElement.attr("id") == null) {
		nextElement = null;
	}
	
	if (nextElement == null) {
		return null;
	}
	
	return commentList.getPosting(nextElement.attr("id"));
}

Posting.prototype.expand = function() {
	var that = this;
	
	withComments("commentlist.full.json", function(commentList) {
		withTemplates(function(templates) {
			if (that.full()) {
				return;
			}
			
			var wasHidden = that.hidden();
			
			if (wasHidden) {
				that.format = "short";
			}
			else {
				that.format = "full";
			}
			
			console.log("expanded " + that.id);
			
			var postingElement = that.element();
			
			postingElement.html(Mustache.render(
					(that.postingType === "reply" ? templates.reply : templates.comment), that, templates));
			
			registerEventHandlers(postingElement);
			
			postingElement.attr("class", that.cssClass());
		});
	});
};

var registerEventHandlers = function(element) {
	var getPosting = function(eventObject) {
		var postingElement = $(eventObject.target);
		
		while (postingElement != null && postingElement.attr("id") == null) {
			postingElement = postingElement.parent();
		}
		
		if (postingElement.attr("id") === "comments") {
			return commentList;
		}
		
		return commentList.getPosting(postingElement.attr("id"));
	};

	$(".commentButton", element).click(function(eventObject) {
		commentList.openReplyFormat();
	});
	
	$(".postButton", element).click(function(eventObject) {
		var posting = getPosting(eventObject);
		
		if (posting.hasOpenReplyForm()) {
			posting.closeReplyForm();
		}
		else {
			posting.openReplyForm();
		}
		
		return false;
	});

	$(".commentList>li", element).click(function(eventObject) {
		var target = $(eventObject.target);
		
		var posting = getPosting(eventObject);
		
		while (posting) {
			var wasHidden = posting.hidden();
			
			posting.expand();

			posting = posting.next();
		}
	});	
	
    $("#postReplyButton").click(function() {
    	var replyTo = $("#replyFormReplyTo").val();
    	var text = $("#replyForm>textarea").val();
    	
        $.ajax({
            url: "/foo",
            dataType: "json",
            data: {
                "post": text,
                "replyTo": replyTo
            },
            type: "POST",
            success: function(json) {
            	var reply = new Reply();
            	
            	reply.id = json.id;
            	reply.format = "full";
            	reply.longText = text;
            	
            	comment = getPosting(replyTo); 
            	
            	comment.replies = comment.replies.prepend(reply); 
            	
            	$("ul", $("#" + replyTo)).children().first().before(
        			"<li class=\"" + reply.cssClass() + "\" id=\"" + json.id + "\">" +
        			Mustach.render(templates.reply, reply, templates) +
        					"</li>");
            }
        });
    });

}

var renderComments = function(templates) {
	var output = Mustache.render(templates.commentlist, commentList, templates);
	
	$("#comments").html(output);
	
    registerEventHandlers($("#comments"));
}


