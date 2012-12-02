(function($) {
	"use strict";

	oj.namespace("page.send.json");

	page.send.json.ComponentsFormController = oj.OjObject.extend({

		numParameters: 0,
		numHeaders: 0,
		$parametersContainer: null,
		$headersContainer: null,
		nameValuePairTemplateHtml: null,
		
		constructor: function(params) {
			this.base(params);
		},

		init: function() {
			var $nameValuePairTemplate;

			this.base();

			this.$parametersContainer = $("#parametersContainer");
			this.$headersContainer = $("#headersContainer");

			$nameValuePairTemplate = $("#nameValuePairTemplate");
			this.nameValuePairTemplateHtml = $nameValuePairTemplate.html(); 
			debugger;
			var x;
		}
	}, {
		className: "page.send.json.ComponentsFormController"
	});

}(jQuery));
