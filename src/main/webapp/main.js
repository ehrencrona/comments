
var profileList = new ProfileList();
var commentList = new CommentList(profileList);

(function(exports) {
	var loaded = {};
	
	exports.withComments = function(url, callback) {
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
		        	
		        	if (setCurrentUser != null) {
		        		setCurrentUser(new Profile().fromJson(json.l));
		        	}
		        	
		        	profileList.fromJson(json.p)
					commentList.fromJson(json.c);
		        	
					callback(commentList);
		        },
		    	error: function(jqXHR, textStatus, errorThrown) {
		    		console.log("Failed to load " + url + ": " + errorThrown);
		    	}
		    });
		}
	};
	
	onUserChange(function() { 
		loaded = {}; 
	});
})(this);

var initializeCommentList = function() {	
	withTemplates(function(templates) {
		renderComments(templates); 

		onUserChange(function() {
			commentList = new CommentList(profileList);
			
			withComments("/service/comments/singleton.json", function() { renderComments(templates) });
		});
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

// TODO: layout-dependency. can we remove this?
Reply.prototype.cssClass = function() {
	var result = "reply-list-item clearfix";
	
	if (this.hidden()) {
		result += " collapsed";
	}
	
	return result;
}

Comment.prototype.cssClass = function() {
	var result = "reviews-list-item clearfix";
	
	if (this.hidden()) {
		result += " collapsed";
	}
	
	return result;
}

Posting.prototype.currentUserCanLike = function() {
	return currentUser && !currentUser.anonymous() && this.favoriteLikerIds && this.favoriteLikerIds.indexOf(currentUser.id) < 0;
}

Profile.prototype.aliasOrYou = function() {
	if (currentUser && currentUser.id === this.id) {
		return "You";
	}
	else {
		return this.alias;
	}
}

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
		if (!this.hasOpenReplyForm()) {
			return;
		}
		
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
	
	    var commentForm = getCommentForm();
	    
	    commentForm.prependTo($("ul.reply-list", this.element()));
	    commentForm.show();
	    
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

Posting.prototype.render = function() {
	var that = this;
	
	withTemplates(function(templates) {		
		var postingElement = that.element();
		
		postingElement.html(
				(that.postingType === "reply" ? templates.reply : templates.comment)(that));
		
		registerEventHandlers(postingElement);
		
		postingElement.attr("class", that.cssClass());
	});
}

Posting.prototype.expand = function() {
	var that = this;
	
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
	
	console.log("expanded " + that.id + " to " + that.format + ".");

	withComments("/service/comments/full/singleton.json", function(commentList) {
		that.render();
		that.element().hide();
		that.element().slideDown(200);
	});
};

var registerEventHandlers = function(element) {
	var getPosting = function(eventObject) {
		var postingElement = $(eventObject.target);
		
		while (postingElement.attr("id") == null) {
			postingElement = postingElement.parent();
		}
		
		return commentList.getPosting(postingElement.attr("id"));
	};
	
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

	$("li.reviews-list-item", element).click(function(eventObject) {
		var target = $(eventObject.target);
		
		var posting = getPosting(eventObject);

		var wasHidden = posting.hidden();

		posting.expand();
		
		while (posting && wasHidden) {
			posting = posting.next();

			wasHidden = posting.hidden();

			if (wasHidden) {
				posting.expand();
			}
		}
	});	
	
    $(".likeButton", element).click(function(eventObject) {
    	var posting = getPosting(eventObject);

    	$.ajax({
            url: "/service/comments/singleton/" + posting.id + ".json",
            dataType: "json",
            data: "{ \"like\": true }",
            type: "POST",
            success: function(json) {
            	posting.fromJson(json.c[0]);
            	profileList.fromJson(json.p);

            	posting.render();            	            	
            },
        	error: function() {
        		console.log("Failed to call reply service.");
        	}
        });
    	
    	return false;
    });	
    
    $("#replyFormButton", element).click(function(eventObject) {
    	var replyTo = $("#replyFormReplyTo").val();
    	var text = $("#replyForm textarea").val();
    	    	
    	if (!text) {
    		// TODO Better error handling.
    		alert("Internal error: Could not find text entered.");
    		return;
    	}
    	
        $.ajax({
            url: "/service/comments/singleton/" + replyTo + ".json",
            dataType: "json",
            data: "{\"text\": \"" + text + "\"}",
            type: "POST",
            success: function(json) {
            	comment = commentList.getPosting(replyTo); 

            	var reply = new Reply(comment).fromJson(json.c[0]);
            	            	
            	comment.closeReplyForm();
            	
            	comment.replies = comment.replies.prepend(reply); 
            	
            	$("#" + replyTo + ">ul").prepend(
        			"<li id=\"" + reply.id + "\"></li>");
            	
            	reply.render();
            	$("#replyForm textarea").val("");
            },
        	error: function() {
        		console.log("Failed to call reply service.");
        	}
        });
    });

    $("#commentFormButton", element).click(function() {
    	var text = $("#commentForm textarea").val();

        $.ajax({
            url: "/service/comments/singleton.json",
            dataType: "json",
            data: "{\"text\": \"" + text + "\"}",
            type: "POST",
            success: function(json) {
            	var comment = new Comment(commentList).fromJson(json.c[0]);
            	            	
            	commentList.add(comment);
            	
            	$(".comment-form-item").after(
        			"<li id=\"" + comment.id + "\" class=\"\"></li>");
            	
            	$("#commentForm textarea").val("");
            	comment.render();
            },
        	error: function() {
        		console.log("Failed to call comment service.");
        	}
        });
    });
}

var renderComments = function(templates) {
	var output = templates.commentlist(commentList);
	
	$("#comments").html(output);
	
    registerEventHandlers($("#comments"));
}
