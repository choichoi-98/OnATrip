$(document).ready(function(){
    //-------------모달 ----------
    var myModal = new bootstrap.Modal(document.getElementById('selectDateModal'),
    {
            keyboard: false
        });
        myModal.show();

    //-------------일정 선택 달력----------
    $(function() {
      $('input[name="datefilter"]').daterangepicker({
          autoUpdateInput: false,
          parentEl: '#calendar', // 캘린더를 붙일 엘리먼트를 지정
          locale: {
              cancelLabel: 'Clear'
          }
      });

      $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
          $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
      });

      $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
          $(this).val('');
      });

      // 캘린더를 자동으로 보여주기 위해 포커스 설정
        $('input[name="datefilter"]').focus();
    });



});//$(document).ready(function(){
