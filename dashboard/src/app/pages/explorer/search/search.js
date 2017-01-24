(function () {
  'use strict';

  angular.module('BlurAdmin.pages.explorer')
    .controller('searchCtrl', searchCtrl);

  /** @ngInject */
  function searchCtrl($scope,$http) {
    $scope.searchtext='';
    //vm.subject = subject;
    //vm.to = to;
    $scope.submit = function() {
          alert("searchtext=");
          
          $http.get("http://192.168.56.101:9001/products")
          .then(function(response) {
            alert(response);
          });

          /*$http.get($rootScope.url+'/metadata/connHierarchy?conn='+$scope.connectionName)
         .then(function(response) {
          $scope.connectionHierarchies = response.data;

          var newEntity = {"dataentity":"New Entity","datafields":[]};
          angular.forEach($scope.connectionHierarchies, function(s){
            s.dataEntities.push(newEntity);
          })

          var newSource = {"datasource" : "New Source", "dataEntities":[]};
          $scope.connectionHierarchies.push(newSource);
          

        });*/
      };
  }
})();
