{{#poster}}{{^anonymous}}
	<div class="profile_pic">
{{/anonymous}}{{/poster}}
{{#full}}
{{#poster}}{{^anonymous}}
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
{{/anonymous}}{{/poster}}
{{/full}}
{{#poster}}{{^anonymous}}
		<span class="name {{^full}}anonymous{{/full}}"><a href="/profile/{{alias}}">{{alias}}</a></span>
	</div>
{{/anonymous}}{{/poster}}