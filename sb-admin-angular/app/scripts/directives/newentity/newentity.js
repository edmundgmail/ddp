'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('sbAdminApp')
	.directive('newentity',function(){
		return {
        templateUrl:'scripts/directives/newentity/newentity.html',
        restrict: 'E',
        replace: true,
        scope: {
        	'selectedDataSource':'@',
      	}//,
      	//controller:function($scope){
        	//alert($scope.selectedDataSource);
      	//}
    	}
	});
