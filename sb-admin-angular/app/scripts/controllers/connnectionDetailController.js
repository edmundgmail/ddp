angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$stateParams', '$http',function($scope, $stateParams,$http){
        $scope.connectionName=$stateParams.connectionName;
        $scope.connectionHierarchies=[];
        $scope.selectedDataEntities=[];
        $http.get('http://192.168.56.101:8881/metadata/connHierarchy?conn='+$scope.connectionName)
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