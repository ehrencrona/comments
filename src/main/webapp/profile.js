
$(document).ready(function() {

	var href = document.location.href;
	
	var profileId = $("#favoriteFormProfileId").val();
			
	$("#favoriteFormButton").click(function() {
		$.ajax({
			url: "/service/profile",
			data: "{\"favorites\": [{\"id\":"+ profileId +"}]}",
			type: "POST"
		});
		
	});
	
	refreshLoginInformation();
});