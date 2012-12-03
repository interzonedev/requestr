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

			this.initializeNameValuePairs(this.$parameterValuesInput, this.$parametersContainer);
			this.initializeNameValuePairs(this.$headerValuesInput, this.$headersContainer);

			oj.$.bindAsEventListener(this.$addParameterTrigger, "click", this, this.addParameter);
			oj.$.bindAsEventListener(this.$addHeaderTrigger, "click", this, this.addHeader);
			oj.$.bindAsEventListener(this.$componentsForm, "submit", this, this.handleFormSubmit);
		},

		initializeNameValuePairs: function($valuesInput, $parameterNameValuePairsContainer) {
			var _this, values, nameValuePairs;

			_this = this;

			values = $valuesInput.val();
			if (values) {
				nameValuePairs = values.split("&"); 

				$.each(nameValuePairs, function(i, nameValuePair) {
					var nameValueParts, name, value;

					nameValueParts = nameValuePair.split("=");
					name = nameValueParts[0];
					value = nameValueParts[1];

					_this.addNameValuePair($parameterNameValuePairsContainer, name, value); 
				});
			}
		},

		addNameValuePair: function($nameValuePairsContainer, name, value) {
			var context, nameValuePairHtml;

			context = {
				count: this.numNameValuePairs
			};

			if (name) {
				context.name = name;
			}

			if (value) {
				context.value = value;
			}

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
			if (this.validateForm()) {
				this.updateValuesFormField(this.$parametersContainer, this.$parameterValuesInput);
				this.updateValuesFormField(this.$headersContainer, this.$headerValuesInput);
			} else {
				evt.preventDefault();
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

		updateValuesFormField: function($parameterNameValuePairsContainer, $valuesInput) {
			var $nameValuePairs, values, value;

			$nameValuePairs = $(".control-nameValuePairContainer", $parameterNameValuePairsContainer);

			values = [];

			$nameValuePairs.each(function(i, nameValuePair) {
				var $nameValuePair, $nameInput, $valueInput;

				$nameValuePair = $(nameValuePair);
				$nameInput = $(".control-nameInput", $nameValuePair);
				$valueInput = $(".control-valueInput", $nameValuePair);
				values.push($nameInput.val() + "=" + $valueInput.val());
			});
			
			value = values.join("&");

			$valuesInput.val(value);
		},

		getActiveInputs: function() {
			return $(".control-nameInput, .control-valueInput").not(".htmlTemplate .control-nameInput, .htmlTemplate .control-valueInput");
		}
	}, {
		className: "page.send.json.ComponentsFormController",

		VALID_INPUT_REGEXP: /\S/
	});

}(jQuery));
