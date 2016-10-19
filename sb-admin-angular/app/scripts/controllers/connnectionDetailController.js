angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$stateParams', function($scope, $stateParams){
        $scope.connectionName=$stateParams.connectionName;
        $scope.dataSources =  $http.get('http://localhost:8881/metadata/dataSources?'+$scope.connectionName);
         .then(function(response) {
          $scope.connections = response.data;
          alert("hello");
        });
}]);
