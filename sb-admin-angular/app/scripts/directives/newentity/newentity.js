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
        scope: {
        	'datasource':'@datasource',
          'connectionName':'@'
      	}
    	}
	});

angular.module('sbAdminApp')
  .directive('cpybooknewentity', function(){
      return {
        templateUrl:'scripts/directives/newentity/cpybooknewentity.html',
      }
  });

angular.module('sbAdminApp')
.controller('newEntityCtrl', ['$scope','$rootScope', '$stateParams', '$http',function($scope, $rootScope, $stateParams,$http){
        $scope.FileTypes=['cpybook', 'csv', 'xml'];

        $scope.copybookFile=null;
        $scope.copybookDataFile=[];

        $scope.setCopybookFile = function(element){
          $scope.copybookFile = element.files[0];
        };
       $scope.setCopybookDataFile = function(element){
          $scope.copybookDataFile.push(element.files);
        };


        $scope.PreviewCopybookFile = function(){
              var formData = new FormData(); 

              var param = {
                'cpyBookName' : $scope.copybookFile.name
              };

              formData.append('param', angular.toJson(param));  
              formData.append('cpybook', $scope.copybookFile);
              angular.forEach($scope.copybookDataFile, function(file){ formData.append(file.name, file)});
              //formData.append('datafile', $scope.copybookDataFile);
          
                $http.post($rootScope.url+'/ingestion', formData, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined }})
              .success(function(response){
                 alert('success');
                }).error(function(){
                  alert('error');            
                });

          };

}]);