
$(document).ready(function() {

	var href = document.location.href;
	
	var profileId = href.substring(href.lastIndexOf("#") + 1);
		
	$("#favoriteFormButton").click(function() {
		$.ajax({
			url: "/service/profile",
			data: "{\"favorites\": [{\"id\":"+ profileId +"}]}",
			type: "POST"
		});
		
	});
});