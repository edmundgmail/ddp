angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$rootScope','$stateParams', '$http',function($scope, $rootScope, $stateParams,$http){
        $scope.connectionName=$stateParams.connectionName;
        $scope.connectionHierarchies=[];
        $scope.selectedDataEntities=[];
        $http.get($rootScope.url+'/metadata/connHierarchy?conn='+$scope.connectionName)
         .then(function(response) {
          $scope.connectionHierarchies = response.data;

          var newEntity = {"dataentity":"New Entity","datafields":[]};
          angular.forEach($scope.connectionHierarchies, function(s){
            s.dataEntities.push(newEntity);
          })

          var newSource = {"datasource" : "New Source", "dataEntities":[]};
          $scope.connectionHierarchies.push(newSource);


        });

$scope.getSelectedDataEntities=function(){
        if($scope.selectedDataSource !=null){
         	var key = $scope.connectionHierarchies.indexOf($scope.selectedDataSource);
         	$scope.selectedDataEntities = $scope.connectionHierarchies[key].dataEntities;         	
         }	
}

}]);