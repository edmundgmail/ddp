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

        $scope.CopybookLayoutDetail = null;

        $scope.CopybookSplitLevels = [ {'id': 0,'name':'No Split'}, {'id': 1, 'name':'On Redefine'}, {'id': 2,'name':'On 01 Level'},{'id': 3,'name':'On Highest Repeating'}];
        $scope.CopybookFileStructure = [{'id': 2, 'name':'Fixed Length'}];
        $scope.CopybookBnaryFormat = [{'id': 1, 'name':'Mainframe'}];
        $scope.CopybookFont = ['cp037'];

        $scope.setCopybookFile = function(element){
          $scope.copybookFile = element.files[0];
        };
       $scope.setCopybookDataFile = function(element){
          $scope.copybookDataFile = [];
          angular.forEach(element.files, function(file){$scope.copybookDataFile.push(file);});
        };


        $scope.PreviewCopybookFile = function(){
              var formData = new FormData(); 
            alert($scope.selectCopybookSplitLevel.name);
            alert($scope.selectCopybookFileStructure.name);
            alert($scope.selectCopybookBinaryFormat.name);
            alert($scope.selectCopybookFont);
            
              var param = {
                'cpyBookName' : $scope.copybookFile.name
              };

              formData.append('param', angular.toJson(param));  
              formData.append('cpybook', $scope.copybookFile);
              //angular.forEach($scope.copybookDataFile, function(file){ formData.append(file.name, file)});
              //formData.append('datafile', $scope.copybookDataFile);
          
                $http.post($rootScope.url+'/ingestion', formData, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined }})
              .success(function(response){
                 $scope.CopybookLayoutDetail = response;
                  alert(response);

                }).error(function(){
                  alert('error');            
                });

          };

}]);