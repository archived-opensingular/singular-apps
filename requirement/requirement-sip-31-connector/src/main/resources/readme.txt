Para gerar stubs:

[sigtaq-client/src/main/java]

wsimport -encoding utf-8 -d . -s . -p org.opensingular.sip.client ../resources/ws_sip_autenticar.wsdl



#Código php para gerar senha

$Senha = '123456';
for($i = 0; $i < strlen($Senha); $i++){
   $Senha[$i] = ~$Senha[$i];
}
$Senha = base64_encode($Senha);
echo $Senha;
