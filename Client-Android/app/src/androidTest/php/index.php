<?php
    class PushNotificationService
    {
        public function __construct()
        {
            header('Content-Type: application/json');
            header("Access-Control-Allow-Origin: *");
            header("Access-Control-Allow-Credentials: true ");
            header("Access-Control-Allow-Methods: OPTIONS, GET, POST");
            header("Access-Control-Allow-Headers: Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
            header("Cache-Control: no-cache, must-revalidate");
            header('Content-Type: application/json; charset=utf-8"');
            header('Authorization: Basic MjQwYmUwZDAtNGU0NC00MDY0LTgyOTEtODE5NDA3MzFiYTk3');
        }


        public static function broadcast($title, $message)
        {
            $curl = curl_init();

            curl_setopt_array($curl, array(
                CURLOPT_URL => 'https://onesignal.com/api/v1/notifications',
                CURLOPT_CUSTOMREQUEST => 'POST',
                CURLOPT_POSTFIELDS =>'{
                "app_id": "42e96cdb-46b2-4d1a-9f73-0fc3e542710e",
                "included_segments": ["Subscribed Users"],
                "headings": {"en": '.$title.'},
                "contents": {"en": '.$message.'}
                }',
                CURLOPT_HTTPHEADER => array(
                    'Content-Type: application/json; charset=utf-8"',
                    'Authorization: Basic MjQwYmUwZDAtNGU0NC00MDY0LTgyOTEtODE5NDA3MzFiYTk3'
                ),
            ));

            $response = curl_exec($curl);


            echo json_encode($response);
        }
    }

    $notification = new PushNotificationService();

    $phone = $_GET['phone'];
    $longitude = $_GET['longitude'];
    $latitude = $_GET['latitude'];

    if($phone != null && $longitude != null && $latitude != null)
    {
        $notification::broadcast(
            "[EMERGENCY]I Need help!",
            "I am at ".$longitude.",".$latitude.">> MyInfo >>".$phone.""
        );
    }
    else
    {
        echo json_encode("Really? Empty parameters?");
    }
?>