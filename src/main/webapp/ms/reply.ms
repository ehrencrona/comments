				{{#hidden}}
					{{#firstHidden}}

						<div class="bubble collapsed"></div>
				
						<div class="bubble collapsed"></div>
				
						<div class="bubble collapsed"></div>

					{{/firstHidden}}
				{{/hidden}}
				{{^hidden}}
					<div class="bubble">
						<p class="message">{{text}}</p>
						{{> like}}
					</div>
			
					<div class="profile_pic"><span class="name"><a
						href="http://www.airbnb.com/users/show/1372184">Andreas</a></span></div>
					
				{{/hidden}}
