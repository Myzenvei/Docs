/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
module.exports = function() {

    return {
        targets: [
            'generateSmarteditKarmaConf',
            'generateSmarteditContainerKarmaConf',
            'generateSmarteditCommonsKarmaConf'
        ],
        config: function(data, conf) {

            const lodash = require('lodash');
            const path = require('path');

            const pathsInBundle = global.smartedit.bundlePaths;
            const karmaSmartedit = require(path.resolve(pathsInBundle.external.generated.webpack.karmaSmartedit));
            const karmaSmarteditContainer = require(path.resolve(pathsInBundle.external.generated.webpack.karmaSmarteditContainer));

            const otmmsmartedit = {
                singleRun: true,

                coverageReporter: {
                    // specify a common output directory
                    dir: 'jsTests/coverage/',
                    reporters: [{
                        type: 'html',
                        subdir: 'report-html'
                    }, {
                        type: 'cobertura',
                        subdir: '.',
                        file: 'cobertura.xml'
                    }]
                },

                junitReporter: {
                    outputDir: 'jsTarget/tests/otmmsmartedit/junit/', // results will be saved as $outputDir/$browserName.xml
                    outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                },

                // list of files / patterns to load in the browser
                files: lodash.concat(
                    pathsInBundle.test.unit.smarteditThirdPartyJsFiles,
                    pathsInBundle.test.unit.commonUtilModules, [
                        'jsTarget/web/features/otmmsmarteditcommons/**/*.+(js|ts)',
                        'jsTarget/web/features/otmmsmartedit/**/*.+(js|ts)',
                        'jsTarget/web/features/otmmsmartedit/templates.js',
                        'jsTests/tests/otmmsmartedit/unit/features/**/*.+(js|ts)'
                    ]
                ),

                // list of files to exclude
                exclude: [
                    'jsTarget/web/features/otmmsmartedit/otmmsmartedit.ts',
                    '**/*.d.ts',
                    '*.d.ts'
                ],

                webpack: karmaSmartedit
            };

            const otmmsmarteditContainer = {
                singleRun: true,

                coverageReporter: {
                    // specify a common output directory
                    dir: 'jsTests/coverage/',
                    reporters: [{
                        type: 'html',
                        subdir: 'report-html'
                    }, {
                        type: 'cobertura',
                        subdir: '.',
                        file: 'cobertura.xml'
                    }]
                },

                junitReporter: {
                    outputDir: 'jsTarget/tests/otmmsmarteditContainer/junit/', // results will be saved as $outputDir/$browserName.xml
                    outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                },

                // list of files / patterns to load in the browser
                files: lodash.concat(
                    pathsInBundle.test.unit.smarteditContainerUnitTestFiles,
                    pathsInBundle.test.unit.commonUtilModules, [
                        'jsTarget/web/features/otmmsmarteditcommons/**/*.+(js|ts)',
                        'jsTarget/web/features/otmmsmarteditContainer/**/*.+(js|ts)',
                        'jsTarget/web/features/otmmsmarteditContainer/templates.js',
                        'jsTests/tests/otmmsmarteditContainer/unit/features/**/*.+(js|ts)'
                    ]
                ),

                // list of files to exclude
                exclude: [
                    'jsTarget/web/features/otmmsmarteditContainer/otmmsmarteditcontainer.ts',
                    '**/*.d.ts',
                    '*.d.ts'
                ],
                webpack: karmaSmarteditContainer
            };

            const otmmsmarteditcommons = {
                singleRun: true,

                coverageReporter: {
                    // specify a common output directory
                    dir: 'jsTests/coverage/',
                    reporters: [{
                        type: 'html',
                        subdir: 'report-html'
                    }, {
                        type: 'cobertura',
                        subdir: '.',
                        file: 'cobertura.xml'
                    }]
                },

                junitReporter: {
                    outputDir: 'jsTarget/tests/otmmsmarteditcommons/junit/', // results will be saved as $outputDir/$browserName.xml
                    outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                },

                // list of files / patterns to load in the browser
                files: lodash.concat(
                    pathsInBundle.test.unit.smarteditThirdPartyJsFiles,
                    pathsInBundle.test.unit.commonUtilModules, [
                        'jsTarget/web/features/otmmsmarteditcommons/**/*.+(js|ts)',
                        'jsTarget/web/features/otmmsmarteditcommons/templates.js',
                        'jsTests/tests/otmmsmarteditcommons/unit/features/**/*.+(js|ts)'
                    ]
                ),

                // list of files to exclude
                exclude: [
                    '**/*.d.ts',
                    '*.d.ts'
                ],

                webpack: karmaSmarteditContainer
            };


            conf.generateSmarteditKarmaConf.data = lodash.merge(otmmsmartedit, conf.generateSmarteditKarmaConf.data);
            conf.generateSmarteditContainerKarmaConf.data = lodash.merge(otmmsmarteditContainer, conf.generateSmarteditContainerKarmaConf.data);

            // Commons is not available in bundle, lets take a copy of the container config to use for the commons
            conf.generateSmarteditCommonsKarmaConf = {
                dest: pathsInBundle.external.generated.karma.smarteditCommons,
                data: lodash.merge(lodash.cloneDeep(conf.generateSmarteditContainerKarmaConf.data), otmmsmarteditcommons)
            };
            conf.generateSmarteditCommonsKarmaConf.data.files = otmmsmarteditcommons.files;


            return conf;
        }
    };
};
