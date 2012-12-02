(function($) {
	"use strict";

	oj.namespace("page.send.json");

	page.send.json.ComponentsFormController = oj.OjObject.extend({

		$componentsForm: null,
		$parameterValuesInput: null,
		$parametersContainer: null,
		$addParameterTrigger: null,
		$headerValuesInput: null,
		$headersContainer: null,
		$addHeaderTrigger: null,
		numNameValuePairs: 0,
		nameValuePairTemplateHtml: null,
		
		constructor: function(params) {
			this.base(params);
		},

		init: function() {
			var $nameValuePairTemplate;

			this.base();

			this.$componentsForm = $("#componentsForm");
			this.$parameterValuesInput = $("#parameterValues");
			this.$parametersContainer = $("#parametersContainer");
			this.$addParameterTrigger = $("#addParameterTrigger");
			this.$headerValuesInput = $("#headerValues");
			this.$headersContainer = $("#headersContainer");
			this.$addHeaderTrigger = $("#addHeaderTrigger");

			$nameValuePairTemplate = $("#nameValuePairTemplate");
			this.nameValuePairTemplateHtml = $nameValuePairTemplate.html(); 

			this.initializeNameValuePairs();

			oj.$.bindAsEventListener(this.$addParameterTrigger, "click", this, this.addParameter);
			oj.$.bindAsEventListener(this.$addHeaderTrigger, "click", this, this.addHeader);
		},

		initializeNameValuePairs: function() {
			this.addNameValuePair(this.$parametersContainer);
			this.addNameValuePair(this.$headersContainer);			
		},

		addNameValuePair: function($nameValuePairsContainer) {
			var context, nameValuePairHtml;

			context = {
				count: this.numNameValuePairs
			};

			nameValuePairHtml = Mustache.to_html(this.nameValuePairTemplateHtml, context);
			
			$nameValuePairsContainer.append(nameValuePairHtml);

			oj.$.bindAsEventListener($(".control-deleteNameValuePair"), "click", this, this.deleteNameValuePair);

			this.numNameValuePairs += 1;
		},

		addParameter: function(evt) {
			evt.preventDefault();

			this.addNameValuePair(this.$parametersContainer);
		},

		addHeader: function(evt) {
			evt.preventDefault();

			this.addNameValuePair(this.$headersContainer);
		},

		deleteNameValuePair: function(evt) {
			var $target, $nameValueContainer;

			evt.preventDefault();

			$target = $(evt.target);
			$nameValueContainer = $target.parent();
			$nameValueContainer.remove();
		}
	}, {
		className: "page.send.json.ComponentsFormController"
	});

}(jQuery));
