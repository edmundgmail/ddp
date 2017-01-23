(function () {
  'use strict';

  angular.module('BlurAdmin.pages.explorer')
    .controller('searchCtrl', searchCtrl);

  /** @ngInject */
  function searchCtrl(text) {
    var vm = this;
    //vm.subject = subject;
    //vm.to = to;
    vm.text = text;

    $scope.submit = function() {
          alert("text=");
        if ($scope.text) {

          $scope.list.push(this.text);
          $scope.text = '';
        }
      };
  }
})();
