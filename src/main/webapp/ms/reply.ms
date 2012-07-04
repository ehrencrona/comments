				{{#hidden}}
					{{#firstHidden}}

						<div class="bubble collapsed"></div>

					{{/firstHidden}}
				{{/hidden}}
				{{^hidden}}
					<div class="bubble">
						<p class="message">{{text}} 
						{{^full}}<span class="date">
							<a class="expand">More</a>
						</span>{{/full}}</p>
						{{#full}}<span class="like">
							{{> like}}
						</span>{{/full}}
					</div>
			
					{{>poster}}
					
				{{/hidden}}
