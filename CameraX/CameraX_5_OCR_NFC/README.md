# CameraX_5_OCR_NFC

#### (1) Android CameraX API와 구글의 MLKIT을 이용해서 OCR을 구현하였습니다. 
#### (2) OCR로부터 얻은 (a)여권번호 (b) 여권만료일 (c) 생일 데이터를 이용해서 여권 NFC 데이터를 가져왔습니다.

#### 설명 : [https://from-android-to-server.tistory.com/80](https://from-android-to-server.tistory.com/81)

#### 스크린샷 및 페이지 구성:

#### 1. 진입 페이지, 2. 카메라로 OCR하는 페이지 3. NFC 읽어오는 페이지 4. 결과 페이지 (이부분은 실제여권으로 테스트 해야해서 개인정보 노출을 피하기 위해 생략)

<p align="center">
<img src="https://github.com/tvroom88/AIO_Android_Kotlin_Support_Material/assets/4710854/30613e1f-96a1-4c65-aca2-78a81c8c2b62" width="20%" height="30%">
</p>
-------------------------------------------------------------------------------------------------------------------------------------------

<br/> <br/> 

<p align="center">
<img src="https://github.com/tvroom88/AIO_Android_Kotlin_Support_Material/assets/4710854/4b230cbf-b87c-43cc-b7ba-58599fe6631c" width="20%" height="30%">
</p>

#### 여권 MRZ라인을 아래에 있는 작은 박스에 넣어야 진행됩니다. 인식을 잘되게 하기 위해 작은 테두리 부분만 잘라서 OCR 되도록 하였습니다.
-------------------------------------------------------------------------------------------------------------------------------------------

<br/> <br/> 

<p align="center">
<img src="https://github.com/tvroom88/AIO_Android_Kotlin_Support_Material/assets/4710854/30127315-bf36-47e2-978c-ce9084fd48ba" width="20%" height="30%">
</p>

#### NFC가 진행이 안되는 경우는 크게 2가지가 있습니다.
1. (a)여권번호 (b) 여권만료일 (c) 생일데이터는 가끔씩 정확하게 안나올때도 있어서 NFC가 진행이 안된다면 데이터를 수정해야합니다.
2. 그것이 아니라면 인식이 완료 되기전에 여권과 거리가 멀어져서 인식이 안될수도 있습니다.
