/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.explorer', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider,$urlRouterProvider) {
    $stateProvider
        .state('explorer', {
          url: '/explorer',
          template : '<ui-view autoscroll="true" autoscroll-body-top></ui-view>',
          abstract: true,
          title: 'Data Explorer',
          sidebarMeta: {
            icon: 'ion-grid',
            order: 0,
          },
        }).state('explorer.search', {
          url: '/search',
          templateUrl: 'app/pages/explorer/search/search.html',
          title: 'Search',
          sidebarMeta: {
            order: 0,
          },
        });
    $urlRouterProvider.when('/explorer','/tables/search');
  }
})();
