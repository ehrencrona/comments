		{{#hasLikers}} 
			{{#hasFavoriteLikers}}
				{{#favoriteLikers}}
					{{aliasOrYou}}{{^last}},{{/last}}
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
