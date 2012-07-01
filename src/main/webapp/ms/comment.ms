{{#hidden}}
	{{#firstHidden}}
		<span style="color: gray">{{hiddenCount}} collapsed</span>
	{{/firstHidden}}
{{/hidden}}
{{^hidden}}
	{{text}} {{#poster}}{{^anonymous}} by {{alias}}{{/anonymous}}{{/poster}} 

	<div>
	{{#full}}
		<a href="#" class="postButton">Reply</a>

		{{> like}}
	{{/full}}
	</div>
	
	<ul>
		{{#replies}}
			<li id="{{id}}" class="{{cssClass}}">
			{{> reply}}
			</li>
		{{/replies}}
	</ul>
{{/hidden}}	
