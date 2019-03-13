package br.senai.sp.agendaandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class MainActivity extends AppCompatActivity {
    Button btn_adicionar;
    ListView lista_contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_adicionar = findViewById(R.id.btn_adicionar);
        lista_contatos = findViewById(R.id.lista_contatos);

        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });

        lista_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = new Contato();

                contato = (Contato)lista_contatos.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, CadastroContatoActivity.class);
                intent.putExtra("contato", (Serializable) contato);
                startActivity(intent);
            }
        });

        registerForContextMenu(lista_contatos);
    }


    public void carregarListView(){
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = new ArrayList<>();
        contatos = dao.getListaContatos();

        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(MainActivity.this, android.R.layout.simple_list_item_1, contatos);

        lista_contatos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final ContatoDAO dao = new ContatoDAO(this);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id_contato = info.position;
        final Contato contato = (Contato)lista_contatos.getItemAtPosition(id_contato);

        new AlertDialog.Builder(this).setTitle("Excluir").setMessage("Deseja Excluir "+contato.getNome()).
        setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.excluir(contato);
                dao.close();
                carregarListView();
            }
        }).setNegativeButton("Não", null).show();




        return super.onContextItemSelected(item);
    }
}
