{{! TODO: Refactor the profile pic to be the same file as used for postings. }}

{{^anonymous}}
	<div class="profile_pic">
		<div class="_pm_container small">
			<div class="_pm_shadow r"></div>
			<div class="_pm_shadow l"></div>
			<div class="_pm">
				<div class="_pm_inner clearfix">
					<div class="_pm_shadow_inner r"></div>
					<div class="_pm_shadow_inner l"></div>
					<div class="_pm_shadow_inner t"></div>
					<div class="_pm_shadow_inner b"></div>
				
					<a href="/profile/{{alias}}"><img
						class="lazy"
						height="68" src="/profilepic/{{alias}}.jpeg" width="68"
						style="display: block;"></a>
				</div>
			</div>
		</div>
		<span class="name"><a href="/profile/{{alias}}">{{alias}}</a></span>
	</div>
{{/anonymous}}

<form id="logoutForm">
<input type="button" id="logoutFormButton" value="Log out"/>
</form>

<div class="clearfix"></div>
