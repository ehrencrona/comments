<div class="profile_header">
	<div class="profile_pic">
		<div class="_pm_container small" style="float:none">
			<div class="_pm">
				<div class="_pm_inner clearfix">
					<div class="_pm_shadow_inner r"></div>
					<div class="_pm_shadow_inner l"></div>
					<div class="_pm_shadow_inner t"></div>
					<div class="_pm_shadow_inner b"></div>
				
					<a href="/profile/{{alias}}"><img
						class="lazy"
						height="136" src="/profilepic/{{alias}}.jpeg" width="136"
						style="display: block;"></a>
				</div>
			</div>
		</div>
	</div>
	
	<h3>{{alias}}</h3>

	<div class="points">{{points}} points</div>

	<form id="favoriteForm">
		<input type="hidden" name="profileId" id="favoriteFormProfileId" value="{{id}}"/> 
		<input type="button" value="Add as favorite" id="favoriteFormButton"/>
	</form>
</div>
	

<div class="clearfix"></div>

<h3>Favorites</h3>

{{#favorites}}
	<div class="favorites">
	<div class="profile_pic">
		<div class="_pm_container small" style="float:none">
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
						height="34" src="/profilepic/{{alias}}.jpeg" width="34"
						style="display: block;"></a>
				</div>
			</div>
		</div>
		<span class="name"><a href="/profile/{{alias}}">{{alias}}</a></span>
	</div>
	</div>
{{/favorites}}

<div class="clearfix"></div>

<h3>Postings</h3>

<div id="comments">
<ul class="reviews-list">
{{#ownPostingWrappers}}
<li class="reviews-list-item clearfix" id="{{id}}">

	{{> comment}}

</li>
{{/ownPostingWrappers}}
</ul>
</div>

Received valuations

<ul>
{{#valuationHistory}}
	<li>{{#valuer}}<a href="/profile/{{alias}}">{{alias}}</a>{{/valuer}} liked "{{#valuedPosting}}{{summarizedText}}{{/valuedPosting}}"  
		{{#date}}<span class="date" data-utc="{{uTCFormatted}}">{{userFormatted}}</span>{{/date}}</li>
{{/valuationHistory}}
</ul>
