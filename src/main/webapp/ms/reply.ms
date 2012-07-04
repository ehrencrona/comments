				{{#hidden}}
					{{#firstHidden}}

						<div class="bubble collapsed"></div>

					{{/firstHidden}}
				{{/hidden}}
				{{^hidden}}
					<div class="bubble">
						<p class="message">{{text}} {{^full}}<a class="expand">More</a>{{/full}}</p>
						{{> like}}
					</div>
			
					{{>poster}}
					
				{{/hidden}}
