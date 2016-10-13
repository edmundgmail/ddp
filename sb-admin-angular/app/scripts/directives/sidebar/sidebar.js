'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */

angular.module('sbAdminApp')
  .directive('sidebar',['$location',function() {
    return {
      templateUrl:'scripts/directives/sidebar/sidebar.html',
      restrict: 'E',
      replace: true,
      scope: {
      },
      controller:function($scope,$http){
        $scope.selectedMenu = 'dashboard';
        $scope.collapseVar = 0;
        $scope.multiCollapseVar = 0;
        //$http.defaults.headers.common['Content-Type']= "application/json";
        var request_url='http://localhost:8881/metadata/connections';
        $http({
        method: 'JSONP',
        url: request_url
      }).success(function(data, status , header, config){
            alert('Success')
      })
      .error(function(data, status , header, config){
            alert('error='+data)
      });
        /*$scope.connections = [
          {
            "name":"spark1"
          },
          {
            "name":"spark2"
          }
        ];*/
        $scope.check = function(x){
          
          if(x==$scope.collapseVar)
            $scope.collapseVar = 0;
          else
            $scope.collapseVar = x;
        };
        
        $scope.multiCheck = function(y){
          
          if(y==$scope.multiCollapseVar)
            $scope.multiCollapseVar = 0;
          else
            $scope.multiCollapseVar = y;
        };
      }
    }
  }]);
