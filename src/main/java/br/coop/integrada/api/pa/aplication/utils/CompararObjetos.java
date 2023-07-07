package br.coop.integrada.api.pa.aplication.utils;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.HistoricoParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;

import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CompararObjetos {   
    
    @SuppressWarnings("rawtypes")
    public static List<HistoricoParametroEstabelecimento> compararParametros(ParametroEstabelecimento parametroAntigo, ParametroEstabelecimento parametroNovo) throws Exception {
        List<HistoricoParametroEstabelecimento> historico = new ArrayList<>();
        if (parametroAntigo.getClass().equals(parametroNovo.getClass())) {
            Class parametro = parametroAntigo.getClass();
            List<Field> todosCampos = Arrays.asList(parametro.getDeclaredFields());

            for (Field field : todosCampos) {
                if (!Modifier.isTransient(field.getModifiers())) {
                    if (field.isAnnotationPresent(ComparaObjeto.class)) {
                        if (!conteudoIgual(field, parametroAntigo, parametroNovo)) {                            
                            ComparaObjeto comparaObjeto = field.getAnnotation(ComparaObjeto.class);
                            if (comparaObjeto.verifica()) {
                                HistoricoParametroEstabelecimento historicoParametroEstabelecimento = new HistoricoParametroEstabelecimento();
                                historicoParametroEstabelecimento.setEstabelecimento(parametroNovo.getEstabelecimento());
                                historicoParametroEstabelecimento.setCampoAlterado(comparaObjeto.nome());
                                historicoParametroEstabelecimento.setValorAnterior(recuperaValorDoCampo(field, parametroAntigo));
                                historicoParametroEstabelecimento.setValorAtual(recuperaValorDoCampo(field, parametroNovo));
                                historicoParametroEstabelecimento.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                                historicoParametroEstabelecimento.setData(LocalDate.now());
                                historicoParametroEstabelecimento.setHora(LocalTime.now());
                                historico.add(historicoParametroEstabelecimento);
                            }
                        }
                    }
                }
            }

        } else {
            throw new Exception("Objetos devem ser iguais!");
        }
        return historico;
    }

    @SuppressWarnings("rawtypes")
    public static String comparar(Object objeto1, Object objeto2) throws Exception {
        StringBuilder retorno = new StringBuilder();
        if (objeto1.getClass().equals(objeto2.getClass())) {
            Class clazz1 = objeto1.getClass();
            List<Field> todosCampos = Arrays.asList(clazz1.getDeclaredFields());
            
            for (Field field : todosCampos) {            	
                if (!Modifier.isTransient(field.getModifiers())) {
                    //Verifica se esta anotado com @ComparaObjeto
                    if (field.isAnnotationPresent(ComparaObjeto.class)) {
                    	
                        if (!conteudoIgual(field, objeto1, objeto2)) {
                        	
                            //retorna a anotacao
                            ComparaObjeto comparaObjeto = field.getAnnotation(ComparaObjeto.class);
                      
                            if (comparaObjeto.verifica()) {

                                retorno.append("Mudou [");
                                retorno.append(comparaObjeto.nome());
                                retorno.append(" de: ");
                                retorno.append("'");
                                retorno.append(recuperaValorDoCampo(field, objeto1));
                                retorno.append("'");
                                retorno.append(" para: ");
                                retorno.append("'");
                                retorno.append(recuperaValorDoCampo(field, objeto2));
                                retorno.append("'");
                                retorno.append("] ");

                            }
                        }
                    }
                }
                
            }

        } else {
            throw new Exception("Objetos diferente!");
        }
        return retorno.toString();
    }
    
    @SuppressWarnings("rawtypes")
	public static String cadastrar(Object objeto1) throws Exception {
        StringBuilder retorno = new StringBuilder();

        if (objeto1.getClass() != null) {
            Class clazz1 = objeto1.getClass();
            List<Field> todosCampos = Arrays.asList(clazz1.getDeclaredFields());
            
            for (Field field : todosCampos) {            	
                if (!Modifier.isTransient(field.getModifiers())) {
                    if (field.isAnnotationPresent(ComparaObjeto.class)) { 
                        ComparaObjeto comparaObjeto = field.getAnnotation(ComparaObjeto.class);                  
                        if (comparaObjeto.verifica()) {
                        	retorno.append(" [");
                            retorno.append(comparaObjeto.nome());
                            retorno.append(": ");
                            retorno.append(recuperaValorDoCampo(field, objeto1));
                            retorno.append("] ");

                        }
                    }
                }
                
            }

        } else {
            throw new Exception("Objetos vazio!");
        }
        return retorno.toString();
    }

    @SuppressWarnings("rawtypes")
    public static Boolean isPossuiAlteracao(Object objeto1, Object objeto2) {
    	try {
    		Boolean isPossuiAlteracao = false;
    		
    		if (objeto1.getClass().equals(objeto2.getClass())) {
    			Class clazz1 = objeto1.getClass();
    			List<Field> todosCampos = Arrays.asList(clazz1.getDeclaredFields());

    			for (Field field : todosCampos) {
    				if (!Modifier.isTransient(field.getModifiers())) {
    					//Verifica se esta anotado com @ComparaObjeto
    					if (field.isAnnotationPresent(ComparaObjeto.class)) {
    						if (!conteudoIgual(field, objeto1, objeto2)) {
								isPossuiAlteracao = true;
    						}
    					}
    				}

    			}
    		}
    		
    		return isPossuiAlteracao;
    	} catch (Exception e) {
    		return true;
    	}
    }

    private static boolean conteudoIgual(Field field, Object object1, Object object2) throws Exception {
        String valor1 = recuperaValorDoCampo(field, object1);
        String valor2 = recuperaValorDoCampo(field, object2);

        if (!valor1.equals(valor2)) {
            return false;
        }
        return true;
    }

    private static String recuperaValorDoCampo(Field field, Object object) throws Exception {
        field.setAccessible(true);
        Object conteudo = field.get(object);

        if (conteudo != null) {
            if(field.getType().getName().equals("java.util.Date")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.mmm");
                return sdf.format(conteudo);
            }
            else {
                return conteudo.toString();
            }
        } else {
            return "";
        }
    }
    
    //Compara duas listas as modificações que ocorreram entre a antiga e a nova
    public static <T> void verificarModificacoes(List<T> listaAntiga, List<T> listaNova){
    	
    	List<T> adicionados = new ArrayList<>(listaNova); 
    	adicionados.removeAll(listaAntiga); // elementos adicionados 
    	List<T> excluidos = new ArrayList<>(listaAntiga); 
    	excluidos.removeAll(listaNova); // elementos excluídos 
    	List<T>	alterados = new ArrayList<>(); 
    	
    	for (T objAntigo : listaAntiga) { 
    		if (listaNova.contains(objAntigo)) { 
    			T objNovo = listaNova.get(listaNova.indexOf(objAntigo)); 
    			if (!objAntigo.equals(objNovo)) { 
    				alterados.add(objNovo); // elementos alterados 
				} 
			} 
		} 
    	// exibir resultados 
    	System.out.println("Adicionados: " + adicionados); 
    	System.out.println("Excluídos: " + excluidos); 
    	System.out.println("Alterados: " + alterados);
    	
    }
    
  //Compara duas listas as modificações que ocorreram entre a antiga e a nova
    public static <T> String verificarModificacoesListas(List<T> listaAntiga, List<T> listaNova){
    	StringBuilder st = new StringBuilder();
    	
    	List<T> adicionados = new ArrayList<>(listaNova); 
    	adicionados.removeAll(listaAntiga); // elementos adicionados 
    	List<T> excluidos = new ArrayList<>(listaAntiga); 
    	excluidos.removeAll(listaNova); // elementos excluídos 
    	List<T>	alterados = new ArrayList<>(); 
    	
    	for (T objAntigo : listaAntiga) {
    		
    		try {
    			
    			Long idAntigo = AbstractEntity.class.cast(objAntigo).getId();
    			
	    		for (T objNovo : listaNova) {
	    			Long idNovo = AbstractEntity.class.cast(objNovo).getId(); //Long.valueOf( objAntigo.getClass().getField("id").toString() );
	    			
	    			//Verifica se os objetos tem o mesmo ID
	    			if(idAntigo.equals(idNovo)) {
		    			
	    				//Posso conparar pois se trata do mesmo registro
	    				
	    				if (!objAntigo.equals(objNovo)) { 
	    	    			
	    					alterados.add(objNovo); // elementos alterados
	    	    				
	    	    			try {
	    	    				st.append(comparar(objAntigo, objNovo));
	    					} 
	    	    			catch (Exception e) {
	    						e.printStackTrace();
	    					}
	    				} 
	    			}
	    		}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
			}	
    	}
    	// exibir resultados 
    	System.out.println("Adicionados: " + adicionados); 
    	System.out.println("Excluídos: " + excluidos); 
    	System.out.println("Alterados: " + alterados);
    	    	
    	
    	if(!adicionados.isEmpty()) {
    		for(T objAdd : adicionados) {
    			st.append("Adicionado: " + objAdd.toString());
    		}
    	}
    	
    	if(!excluidos.isEmpty()) {
    		for(T objExcluido : excluidos) {
    			st.append("Excluído: " + objExcluido.toString());
    		}
    	}
    	
    	return st.toString();
    	
    }
    
    public static StatusIntegracao verificarListas(List<? extends AbstractEntity> listaAntiga, List<? extends AbstractEntity> listaNova){
    	
    	StatusIntegracao statusReturn = StatusIntegracao.INTEGRADO;
    	
    	List excluidos = new ArrayList<>();
    	
    	for(AbstractEntity itemAntigo : listaAntiga){
    		
    		if(itemAntigo.getDataInativacao() != null){
    			//excluidos.add(itemAntigo);
    			continue;
    		}
    		
    		//Boolean encontrou = false;
    		for(AbstractEntity itemNovo : listaNova){
    			if(itemNovo.getId() == null && itemNovo.getStatusIntegracao() == null ) {
    				itemNovo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    				statusReturn = StatusIntegracao.INTEGRAR;
    			}
    			else if(itemAntigo.getId().equals(itemNovo.getId())) {
    				if(isPossuiAlteracao(itemAntigo, itemNovo)) {
    					itemNovo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    					statusReturn = StatusIntegracao.INTEGRAR;
    				}
    				//encontrou = true;
    			}
    				
    			
    		}
    		/*if(!encontrou){
    			itemAntigo.setDataInativacao( new Date() );
    			itemAntigo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    			statusReturn = StatusIntegracao.INTEGRAR;
    			//excluidos.add(itemAntigo);
    		}*/
    	}
    	
    	//if(!excluidos.isEmpty())
    		//listaNova.addAll(excluidos);
    	
    	return statusReturn;
    	//return (List<T>) listaNova;
    	
    	/*
    	//incluidos.removeAll(listaAntiga); // elementos adicionados
    	for(AbstractEntity item : listaNova){
    		if(item.getId() == null) {
    			item.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    			incluidos.add((T) item);
    		}
    		else {
    			if(listaAntiga.stream().filter( p -> p.getId().equals(item.getId())).findFirst().orElse(null) == null){
        			//o item da lista nova não está na velha e ocorreu uma inclusão
    				item.setStatusIntegracao(StatusIntegracao.INTEGRAR);
        			incluidos.add((T) item);
        		} 
    		}
    	}
    	
    	return incluidos;
    	*/
    }
    
    
    
    public static <T> List<T> verificarExcluidos(List<? extends AbstractEntity> listaAntiga, List<? extends AbstractEntity> listaNova){
    	List<T> excluidos = new ArrayList<>();
    	
    	for(AbstractEntity itemAntigo : listaAntiga){
    		if(itemAntigo.getId() == null) {
    			continue;
    		}
    		
    		//AbstractEntity temp = listaNova.stream().filter( p -> p.getId().equals( itemAntigo.getId() )).findFirst().orElse(null);
    		AbstractEntity temp = null;
    		for(AbstractEntity itemNovo : listaNova){
    			if(itemNovo.getId() != null) {
    				if(itemAntigo.getId().equals(itemNovo.getId())) {
    					temp = itemNovo;
    				}
    			}
    		}
    		
    		if(temp == null) {
    			itemAntigo.setDataInativacao( new Date() );
    			itemAntigo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    			excluidos.add( (T) itemAntigo);
    		}
    	}
    	    	
    	return excluidos;
    }
    
    public static <T> List<T> verificarAlterados(List<? extends AbstractEntity> listaAntiga, List<? extends AbstractEntity> listaNova){
    	List<T> alterados = new ArrayList<>();
    	
    	for(AbstractEntity itemAntigo : listaAntiga){
    		if(itemAntigo.getId() == null) {
    			continue;
    		}
    		
    		AbstractEntity itemNovo = listaNova.stream().filter( p -> p.getId().equals(itemAntigo.getId()) ).findFirst().orElse(null);
    		
    		if(itemNovo != null) {
    			if(isPossuiAlteracao(itemAntigo, itemNovo)) {
    				itemNovo.setDataAtualizacao(new Date());
    				itemNovo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    				alterados.add((T)itemNovo);
    			}
    		}
    	}
    	
    	return alterados;
    }
    
    
    
}
