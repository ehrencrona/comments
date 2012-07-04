		{{#hasLikers}} 
			{{#hasFavoriteLikers}}
				{{#favoriteLikers}}
				{{#this}}
					{{aliasOrYou}}{{^last}},{{/last}}
				{{/this}}
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
		
		{{#currentUserCanLike}}
			<a href="#" class="likeButton">Like</a>
		{{/currentUserCanLike}}
