
$(document).ready(function() {

	var href = document.location.href;
	
	var profileId = href.substring(href.lastIndexOf("#") + 1);
	
	// TODO: what if there is no ID in the URL?
	
	$(document).ready(function() {
	    $.ajax({
	        url: "/service/profile/" + profileId,
	        dataType: "json",
	        success: function(json) {
	        	withTemplates(function(templates) {
	        		var output = Mustache.render(templates.profile, json, templates);
	        		$("#profile").html(output);
	        		
	        		$("#favoriteFormButton").click(function() {
	        			$.ajax({
	        				url: "/service/profile",
	        				data: "{\"favorites\": [{\"id\":"+ profileId +"}]}",
	        				type: "POST"
	        			});
	        			
	        		});
	        	});
	        }
	    });
	});	
});