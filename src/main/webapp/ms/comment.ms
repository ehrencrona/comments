{{#hidden}}
	{{#firstHidden}}
		<span style="color: gray">{{hiddenCount}} collapsed</span>
	{{/firstHidden}}
{{/hidden}}
{{^hidden}}
	{{text}}
	
	<ul>
		{{#replies}}
			<li id="{{id}}" class="{{cssClass}}">
			{{> reply}}
			</li>
		{{/replies}}
	</ul>

	{{#full}}
		<a href="#" class="postButton">Reply</a>

		{{#hasLikers}} 
			{{#hasFavoriteLikers}}
				{{#favoriteLikers}}
					{{alias}} {{^last}},{{/last}}
				{{/favoriteLikers}}
				
				{{#hasOtherLikers}}
					and
				{{/hasOtherLikers}}
			{{/hasFavoriteLikers}}
					
			{{#hasOtherLikers}}
				{{otherLikerCount}} others 
			{{/hasOtherLikers}}		
			
			liked this.
		{{/hasLikers}}
		
		{{^currentUserLikes}}	
			<a href="#" class="likeButton">Like</a>
		{{/currentUserLikes}}
	{{/full}}
{{/hidden}}	
