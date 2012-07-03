// TODO Use underscore.js or prototype.js.

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

var Profile = function() {
}

Profile.prototype.fromJson = function(json) {
	if (json.length > 0) {
		this.id = json[0];
		this.alias = json[1];
	}
	
	return this;
}

Profile.prototype.anonymous = function() {
	return this.id == null;
}

var Posting = function() {
}

Posting.prototype.hidden = function() {
	return this.format === "hidden";
}

Posting.prototype.full = function() {
	return this.format === "full";
}

Posting.prototype.visit = function(callback) {
	callback(this);
}

Posting.prototype.poster = function() {
	return this.commentList.profileList.getProfile(this.posterId);
}

var Comment = function(commentList) {
	this.commentList = commentList;
	this.replies = [];
}

Comment.prototype = new Posting();

Comment.prototype.postingType = "comment"; 

Comment.prototype.hasReplies = function() {
	return this.replies != null && this.format === "full";
}

Comment.prototype.visit = function(callback) {
	callback(this);

	this.replies.foreach(function(reply) { reply.visit(callback); });
}

var Reply = function(comment) {
	this.commentList = comment.commentList;
}

Reply.prototype = new Posting();

Reply.prototype.postingType = "reply"; 

var updateLastHidden = function(comments) {
	var lastHidden = false;
	var firstHidden = null;
	
	comments.foreach(function(comment) {
		if (comment.hidden()) {
			if (!lastHidden) {
				comment.firstHidden = true;
				comment.hiddenCount = 1;
				firstHidden = comment;
			}
			else {
				firstHidden.hiddenCount++;
			}
		}
		else {
			comment.firstHidden = null; 
		}
		
		lastHidden = comment.hidden();		
	});
}

var ProfileList = function() {
	this.profileById = {};
}

ProfileList.prototype.fromJson = function(json) {
	var that = this;
	
	json.foreach(function(profileJson) {
		that.profileById[profileJson[0]] = new Profile().fromJson(profileJson);
	});

	return this;
}

ProfileList.prototype.getProfile = function(profileId) {
	return this.profileById[profileId];
}

var CommentList = function(profileList) {
	this.comments = [];

	if (!profileList) {
		profileList = new ProfileList();
	}
	
	this.profileList = profileList;
}

CommentList.prototype.add = function(comment) {
	this.comments = this.comments.prepend(comment);

	if (this.postingById) {
		this.postingById[comment.id] = comment;
	}
}

CommentList.prototype.getPosting = function(id) {
	if (!this.postingById) {
		var that = this;
		
		this.postingById = {};
		
		this.visit(
			function(comment) {
				that.postingById[comment.id] = comment;
			});
	}
	
	return this.postingById[id];
}

CommentList.prototype.foreach = function(callback) {
	this.comments.foreach(callback);
}

CommentList.prototype.visit = function(callback) {
	this.foreach(function(comment) {
		callback(comment);

		comment.visit(callback);
	})
}

CommentList.prototype.fromJson = function(json) {
	var that = this;
		
	var update = this.comments.length > 0;
	
	if (update) {
		json.foreach(function(commentJson) {
			var posting = that.getPosting(commentJson[0]);
			
			if (posting != null) {
				posting.fromJson(commentJson);
			}
		});
	}
	else {
		json.foreach(function(commentJson) {
			that.comments.push(new Comment(that).fromJson(commentJson));
		});
	}
		
	updateLastHidden(this.comments);
	
	this.postingById = null;
	
	return this;
}

Posting.prototype.fromJson = function(json) {
	this.id = json[0];
	
	// don't modify format on updates; the format reflects the state of the DOM.
	if (this.format == null) {
		this.format = json[1];
	}

	if (this.format == null) {
		this.format = "hidden";
	}

	this.shortText = json[2];
	this.longText = json[3];
	this.posterId = json[4];

	if (json[5]) {
		this.favoriteLikerIds = json[5][0]; 
		this.otherLikerCount = json[5][1]; 
	}
	
	return this;
}

Posting.prototype.favoriteLikers = function() {
	var that = this;
	
	var result = this.favoriteLikerIds.map(function(id) {
		var profile = that.commentList.profileList.getProfile(id);
		
		if (profile == null) {
			console.log("Uknown profile " + id + " encountered as favorite liker of " + this.id);
			
			profile = new Profile();
		}
		
		return profile;
	});
	
	if (result.length > 0) {
		result[result.length - 1].last = true;
	}
	
	return result;
}

Posting.prototype.hasLikers = function() {
	return this.hasFavoriteLikers() || this.hasOtherLikers();
}

Posting.prototype.hasFavoriteLikers = function() {
	return this.favoriteLikerIds && this.favoriteLikerIds.length > 0;
}

Posting.prototype.hasOtherLikers = function() {
	return this.otherLikerCount > 0;
}

Posting.prototype.text = function() {
	if (this.format === "full") {
		return this.longText;
	}
	else {
		return this.shortText;
	}
}

Comment.prototype.fromJson = function(json) {
	var that = this;

	Posting.prototype.fromJson.apply(this, [json]);
	
	var replyJson = json[6];
	
	if (replyJson != null) {
		if (this.replies.length > 0) {
			var jsonById = {};
			
			replyJson.foreach(function(json) { 
				jsonById[json[0]] = json;
			});

			this.visit(function(reply) {
				var json = jsonById[reply.id];
				
				if (json != null) {
					reply.fromJson(json);
				}
			});
		}
		else {
			replyJson.foreach(function(json) { 
				that.replies.push(new Reply(that).fromJson(json)); 
			});
		}
	}
	
	updateLastHidden(this.replies);

	return this;
}
