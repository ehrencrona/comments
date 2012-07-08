
{{^anonymous}}
	<div class="profile_pic">
{{/anonymous}}

{{^anonymous}}
		<div class="_pm_container small">
			<div class="_pm_shadow r"></div>
			<div class="_pm_shadow l"></div>
			<div class="_pm">
				<div class="_pm_inner clearfix">
					<div class="_pm_shadow_inner r"></div>
					<div class="_pm_shadow_inner l"></div>
					<div class="_pm_shadow_inner t"></div>
					<div class="_pm_shadow_inner b"></div>
				
					<a href=""><img
						class="lazy"
						height="68" src="/profilepic/{{id}}.jpeg" width="68"
						style="display: block;"></a>
				</div>
			</div>
		</div>
{{/anonymous}}
{{^anonymous}}
		<span class="name"><a href="">{{alias}}</a></span>
	</div>
{{/anonymous}}

<form id="logoutForm">
<input type="button" id="logoutFormButton" value="Log out"/>
</form>

<div class="clearfix"></div>
