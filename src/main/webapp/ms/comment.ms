{{#hidden}}
	{{#firstHidden}}
		<span style="color: gray">{{hiddenCount}} collapsed</span>
	{{/firstHidden}}
{{/hidden}}
{{^hidden}}


{{#poster}}{{^anonymous}}
	<div class="profile_pic">
{{#full}}
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
			data-original="https://a0.muscache.com/users/1372184/profile_pic/1320478176/medium.jpg"
			height="68" src="./index_files/medium.jpg" title="Andreas" width="68"
			style="display: block;"></a></div>
		</div>
		</div>
{{/full}}
		<span class="name"><a
			href="">{{alias}}</a></span>
	</div>
{{/anonymous}}{{/poster}}
	<div class="bubble">
		<p class="message">{{text}}</p>

		{{#full}}
		<span class="like">
			{{> like}} 
		</span>

		<span class="date">
			<a href="#" class="postButton">Reply</a>

			Yesterday, 12:00 
		</span>
		{{/full}}
	</div>

	<div class="clearfix"></div>

	<ul class="reply-list">

		{{#replies}}
			<li class="{{cssClass}}" id="{{id}}">
			{{> reply}}
			</li>
		{{/replies}}
	</ul>
{{/hidden}}	
