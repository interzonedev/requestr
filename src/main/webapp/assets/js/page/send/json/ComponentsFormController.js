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
			oj.$.bindAsEventListener(this.$componentsForm, "submit", this, this.handleFormSubmit);
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
		},

		handleFormSubmit: function(evt) {
			if (!this.validateForm()) {
				evt.preventDefault();	
			} else {
				// Update hidden form fields.
			}		
		},

		validateForm: function() {
			var _this, valid, $inputs;

			_this = this;

			valid = true;

			this.clearFormErrors();

			$inputs = this.getActiveInputs();

			$inputs.each(function(i, input) {
				var $input, $inputGroup, $errorContainer, $nameValueContainer;

				$input = $(input);
				if (!_this.clazz.VALID_INPUT_REGEXP.test($input.val())) {
					valid = false;
					$inputGroup = $input.parent();
					$errorContainer = $(".control-formError", $inputGroup);
					$nameValueContainer = $inputGroup.parent();
					$nameValueContainer.addClass("error");
					$errorContainer.show();
				}
			});

			return valid;
		},

		clearFormErrors: function() {
			var $inputs;

			$inputs = this.getActiveInputs();

			$inputs.each(function(i, input) {
				var $input, $inputGroup, $errorContainer, $nameValueContainer;

				$input = $(input);
				$inputGroup = $input.parent();
				$errorContainer = $(".control-formError", $inputGroup);
				$nameValueContainer = $inputGroup.parent();
				$nameValueContainer.removeClass("error");
				$errorContainer.hide();
			});
		},

		getActiveInputs: function() {
			return $(".control-input").not(".htmlTemplate .control-input");
		}
	}, {
		className: "page.send.json.ComponentsFormController",
		VALID_INPUT_REGEXP: /\S/
	});

}(jQuery));
