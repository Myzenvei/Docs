/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
import * as angular from 'angular';
import 'otmmsmarteditContainer/otmmsmarteditContainer_bundle.js';

angular.module('otmmsmarteditContainer', ['featureServiceModule', 'yjqueryModule'])
	.run(function(featureService: any, yjQuery: any) {
		'ngInject';


		let loadCSS = function(href: string) {
			let cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
			yjQuery("head").append(cssLink);
		};
		loadCSS("/otmmsmartedit/style/otmmsmartedit.css");

	});
