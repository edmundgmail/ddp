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
        $scope.CopybookSplitLevels = [ {'id': 0,'name':'SplitNone'}, {'id': 1, 'name':'SplitRedefine'}, {'id': 2,'name':'Split01Level'},{'id': 3,'name':'SplitHighestRepeating'}];
        $scope.CopybookFileStructure = [{'id': 2, 'name':'FixedLength'}];
        $scope.CopybookBnaryFormat = [{'id': 1, 'name':'FMT_MAINFRAME'}];
        $scope.CopybookFont = ['cp037'];

        $scope.selectCopybookSplitLevel=null;
        $scope.selectCopybookFileStructure=null;
        $scope.selectCopybookBinaryFormat=null;
        $scope.selectCopybookFont=null;
        $scope.copybookFile=null;
        $scope.copybookDataFile=[];

        $scope.setCopybookFile = function(element){
          $scope.copybookFile = element.files[0];
        };



        $scope.PreviewCopybookFile = function(){
              alert('Please input all the fields');

              var formData = new FormData(); 

              var param = {
                'cpyBookName' : $scope.copybookFile.name,
                'cpybookFont': $scope.selectCopybookFont,
                'fileStructure': $scope.selectCopybookFileStructure.name,
                'binaryFormat' : $scope.selectCopybookBinaryFormat.name,
                'splitOptoin': $scope.selectCopybookSplitLevel.name
              };

              formData.append('param', angular.toJson(param));  
              formData.append($scope.copybookFile.name, $scope.copybookFile);  
          
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