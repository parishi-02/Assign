<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.5/css/jquery.dataTables.min.css"/>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script>
    // Function to go back one page in history
    function goBack() {
        window.history.back();
    }

    $(document).ready(function() {
                // Bind blur event to all required fields
                $('input[required], select[required]').blur(function() {
                    // Check if the field is empty
                    if (!$(this).val().trim()) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid'); // Remove is-invalid if field is filled
                    }
                });

                // On form submit, validate all required fields
                $('form').submit(function(event) {
                    // Remove all previous validation errors
                    $('.is-invalid').removeClass('is-invalid');
                });
            });

</script>