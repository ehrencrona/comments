{{#hidden}}
	{{#firstHidden}}
		<span style="color: gray">{{hiddenCount}} collapsed</span>
	{{/firstHidden}}
{{/hidden}}
{{^hidden}}
	{{text}}

	{{#hasReplies}}
	
	<ul>
		{{#replies}}
			<li id="{{id}}" class="{{cssClass}}">
			{{> reply}}
			</li>
		{{/replies}}
	</ul>

	{{/hasReplies}}

	{{#full}}
		<a href="#" class="postButton">Reply</a>
	{{/full}}
{{/hidden}}	
